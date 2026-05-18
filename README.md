# Realtime EN→ZH Speech Translation (WhisperLive)

本项目已重建为 Python 工程：
- 使用 **WhisperLive** 服务做英文实时识别。
- 使用 **FastAPI + WebSocket** 中转音频与识别结果。
- 使用 `deep-translator` 将英文实时翻译为中文。
- 前端支持手机与 PC 自适配。

## 1) 安装

```bash
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
```

## 2) 启动 WhisperLive 服务

请先在本机启动 WhisperLive（默认 ws://127.0.0.1:9090）：

```bash
python run_server.py --port 9090 --backend faster_whisper --max_clients 4
```

> 你也可以用环境变量 `WHISPERLIVE_WS_URL` 指向其它地址。

## 3) 启动 Web 应用

```bash
uvicorn app.main:app --host 0.0.0.0 --port 8000 --reload
```

打开：`http://127.0.0.1:8000`

## 4) 使用说明

1. 点击“开始录音”。
2. 对着麦克风说英语。
3. 页面会实时显示英文与中文翻译。
