import asyncio
import json
import os
from pathlib import Path

from deep_translator import GoogleTranslator
from fastapi import FastAPI, WebSocket, WebSocketDisconnect, Request
from fastapi.responses import HTMLResponse
from fastapi.staticfiles import StaticFiles
from fastapi.templating import Jinja2Templates
from websockets.client import connect as ws_connect

BASE_DIR = Path(__file__).resolve().parent.parent
TEMPLATES_DIR = BASE_DIR / "templates"
STATIC_DIR = BASE_DIR / "static"

WHISPERLIVE_WS_URL = os.getenv("WHISPERLIVE_WS_URL", "ws://127.0.0.1:9090")
TRANSLATOR = GoogleTranslator(source="en", target="zh-CN")

app = FastAPI(title="WhisperLive Realtime EN→ZH Translator")
app.mount("/static", StaticFiles(directory=STATIC_DIR), name="static")
templates = Jinja2Templates(directory=str(TEMPLATES_DIR))


@app.get("/", response_class=HTMLResponse)
async def index(request: Request):
    return templates.TemplateResponse("index.html", {"request": request, "ws_url": "/ws/translate"})


@app.websocket("/ws/translate")
async def ws_translate(client_ws: WebSocket):
    await client_ws.accept()

    try:
        async with ws_connect(WHISPERLIVE_WS_URL, max_size=None) as whisper_ws:
            config = {
                "uid": "mobile_web_client",
                "language": "en",
                "task": "transcribe",
                "model": "small",
                "use_vad": True,
            }
            await whisper_ws.send(json.dumps(config))

            async def client_to_whisper():
                while True:
                    message = await client_ws.receive()
                    if "bytes" in message and message["bytes"] is not None:
                        await whisper_ws.send(message["bytes"])
                    elif "text" in message and message["text"]:
                        data = json.loads(message["text"])
                        if data.get("type") == "stop":
                            await whisper_ws.close()
                            break

            async def whisper_to_client():
                while True:
                    raw = await whisper_ws.recv()
                    if isinstance(raw, bytes):
                        continue

                    payload = json.loads(raw)
                    transcript = payload.get("text") or payload.get("segments", [{}])[-1].get("text", "")
                    transcript = transcript.strip()
                    if not transcript:
                        continue

                    translated = TRANSLATOR.translate(transcript)
                    await client_ws.send_json(
                        {
                            "type": "result",
                            "en": transcript,
                            "zh": translated,
                            "is_final": payload.get("is_final", False),
                        }
                    )

            await asyncio.gather(client_to_whisper(), whisper_to_client())
    except WebSocketDisconnect:
        pass
    except Exception as exc:
        await client_ws.send_json({"type": "error", "message": str(exc)})
    finally:
        await client_ws.close()
