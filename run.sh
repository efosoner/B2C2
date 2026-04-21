#!/usr/bin/env bash
set -euo pipefail

echo ""
echo " ============================================"
echo "  B2C2 Website Test Automation"
echo " ============================================"
echo ""

# --- Check Java ---
if ! command -v java &>/dev/null; then
    echo " [ERROR] Java not found. Please install Java 17 or higher."
    echo "         Download: https://adoptium.net/"
    exit 1
fi
JAVA_VER=$(java -version 2>&1 | head -1 | awk -F '"' '{print $2}')
echo " [OK] Java found: $JAVA_VER"

# --- Auto-detect browser ---
BROWSER=""
BROWSER_NAME=""

# Check Chrome
for path in \
    "$(command -v google-chrome 2>/dev/null || true)" \
    "$(command -v google-chrome-stable 2>/dev/null || true)" \
    "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome" \
    "$(command -v chromium 2>/dev/null || true)" \
    "$(command -v chromium-browser 2>/dev/null || true)"; do
    if [[ -n "$path" && -x "$path" ]]; then
        BROWSER="chrome"
        BROWSER_NAME="Google Chrome"
        break
    fi
done

# Check Firefox
if [[ -z "$BROWSER" ]]; then
    if command -v firefox &>/dev/null || [[ -x "/Applications/Firefox.app/Contents/MacOS/firefox" ]]; then
        BROWSER="firefox"
        BROWSER_NAME="Mozilla Firefox"
    fi
fi

# Check Edge (macOS / Linux)
if [[ -z "$BROWSER" ]]; then
    for path in \
        "$(command -v microsoft-edge 2>/dev/null || true)" \
        "$(command -v microsoft-edge-stable 2>/dev/null || true)" \
        "/Applications/Microsoft Edge.app/Contents/MacOS/Microsoft Edge"; do
        if [[ -n "$path" && -x "$path" ]]; then
            BROWSER="edge"
            BROWSER_NAME="Microsoft Edge"
            break
        fi
    done
fi

if [[ -z "$BROWSER" ]]; then
    echo " [ERROR] No supported browser found."
    echo "         Please install one of: Google Chrome, Mozilla Firefox, or Microsoft Edge."
    exit 1
fi

echo " [OK] Browser found: $BROWSER_NAME"
echo ""

# --- Parse arguments ---
MODE="headless"
SLOW=0
case "${1:-}" in
    headed)  MODE="headed"; SLOW=1500 ;;
    demo)    MODE="demo";   SLOW=3000 ;;
esac

HEADLESS_FLAG="true"
[[ "$MODE" != "headless" ]] && HEADLESS_FLAG="false"

echo " Running tests..."
echo " Browser: $BROWSER_NAME"
case "$MODE" in
    headless) echo " Mode:    Headless (fast)" ;;
    headed)   echo " Mode:    Headed with slow-motion (1.5s per step)" ;;
    demo)     echo " Mode:    Demo (3s per step)" ;;
esac
echo ""
echo " ============================================"
echo ""

./mvnw clean test -Dbrowser="$BROWSER" -Dheadless="$HEADLESS_FLAG" -Dslow="$SLOW"

echo ""
echo " ============================================"
echo "  ALL TESTS PASSED"
echo " ============================================"
echo ""
