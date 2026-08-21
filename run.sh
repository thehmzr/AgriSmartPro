#!/usr/bin/env bash
# Sets up the Python environment for the backend and starts the server.
# Safe to run again; it skips whatever is already done.
set -euo pipefail

cd "$(dirname "$0")/backend"

PORT=5000
SETUP_ONLY=0
FRESH=0

while [ $# -gt 0 ]; do
    case "$1" in
        --port)  PORT="$2"; shift 2 ;;
        --setup) SETUP_ONLY=1; shift ;;
        --fresh) FRESH=1; shift ;;
        -h|--help)
            echo "usage: ./run.sh [--port N] [--setup] [--fresh]"
            echo "  --port N   listen on N instead of 5000"
            echo "  --setup    install dependencies but don't start the server"
            echo "  --fresh    rebuild the virtualenv from scratch"
            exit 0 ;;
        *) echo "unknown option: $1" >&2; exit 1 ;;
    esac
done

PY=""
for c in python3.11 python3.10 python3.9 python3; do
    if command -v "$c" >/dev/null 2>&1; then
        v=$("$c" -c 'import sys; print("%d%02d" % sys.version_info[:2])')
        if [ "$v" -ge 308 ] && [ "$v" -le 311 ]; then PY="$c"; break; fi
    fi
done

if [ -z "$PY" ]; then
    echo "Need Python 3.8-3.11. scikit-learn 1.2.2, which the saved models" >&2
    echo "were trained with, has no build for 3.12 or newer." >&2
    exit 1
fi

[ "$FRESH" -eq 1 ] && rm -rf .venv

if [ ! -d .venv ]; then
    echo "creating virtualenv with $PY"
    "$PY" -m venv .venv
    .venv/bin/pip install --quiet --upgrade pip
fi

if ! .venv/bin/python -c "import flask, sklearn, pandas" >/dev/null 2>&1; then
    echo "installing dependencies"
    .venv/bin/pip install --quiet -r requirements.txt
fi

if [ "$SETUP_ONLY" -eq 1 ]; then
    echo "ready. start it with ./run.sh"
    exit 0
fi

echo "starting on http://127.0.0.1:$PORT  (ctrl+c to stop)"
exec .venv/bin/flask --app app run --port "$PORT"
