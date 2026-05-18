const startBtn = document.getElementById('startBtn');
const stopBtn = document.getElementById('stopBtn');
const statusEl = document.getElementById('status');
const enEl = document.getElementById('enText');
const zhEl = document.getElementById('zhText');

let ws;
let mediaRecorder;

function appendText(el, text, isFinal) {
  if (isFinal) {
    el.textContent += (el.textContent ? '\n' : '') + text;
  } else {
    const lines = el.textContent.split('\n').filter(Boolean);
    if (lines.length === 0) {
      el.textContent = text;
    } else {
      lines[lines.length - 1] = text;
      el.textContent = lines.join('\n');
    }
  }
}

async function start() {
  const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
  ws = new WebSocket(`${location.origin.replace('http', 'ws')}/ws/translate`);
  ws.binaryType = 'arraybuffer';

  ws.onopen = () => {
    statusEl.textContent = '已连接，录音中';
    startBtn.disabled = true;
    stopBtn.disabled = false;

    mediaRecorder = new MediaRecorder(stream, { mimeType: 'audio/webm;codecs=opus' });
    mediaRecorder.ondataavailable = async (evt) => {
      if (evt.data.size > 0 && ws.readyState === WebSocket.OPEN) {
        const buf = await evt.data.arrayBuffer();
        ws.send(buf);
      }
    };
    mediaRecorder.start(250);
  };

  ws.onmessage = (event) => {
    const data = JSON.parse(event.data);
    if (data.type === 'result') {
      appendText(enEl, data.en, data.is_final);
      appendText(zhEl, data.zh, data.is_final);
    }
    if (data.type === 'error') {
      statusEl.textContent = `错误: ${data.message}`;
    }
  };

  ws.onclose = () => {
    statusEl.textContent = '连接已关闭';
    startBtn.disabled = false;
    stopBtn.disabled = true;
  };
}

function stop() {
  if (mediaRecorder && mediaRecorder.state !== 'inactive') mediaRecorder.stop();
  if (ws && ws.readyState === WebSocket.OPEN) {
    ws.send(JSON.stringify({ type: 'stop' }));
    ws.close();
  }
}

startBtn.addEventListener('click', start);
stopBtn.addEventListener('click', stop);
