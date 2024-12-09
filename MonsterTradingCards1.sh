#!/bin/sh

# --------------------------------------------------
# Monster Trading Cards Game
# --------------------------------------------------
echo "CURL Testing for Monster Trading Cards Game"
echo "Syntax: MonsterTradingCards.sh [pause]"
echo "- pause: optional, if set, then script will pause after each block"
echo .


pauseFlag=0
for arg in "$@"; do
    if [ "$arg" == "pause" ]; then
        pauseFlag=1
        break
    fi
done

# --------------------------------------------------
echo "8) show all acquired cards kienboec"
curl -i -X GET http://localhost:10001/cards --header "Authorization: Bearer kienboec-mtcgToken"
echo "Should return HTTP 200 - and a list of all cards"
echo "should fail (no token):"
curl -i -X GET http://localhost:10001/cards
echo "Should return HTTP 4xx - Unauthorized"
echo .
echo .

if [ $pauseFlag -eq 1 ]; then read -p "Press enter to continue..."; fi

# --------------------------------------------------
echo "9) show all acquired cards altenhof"
curl -i -X GET http://localhost:10001/cards --header "Authorization: Bearer altenhof-mtcgToken"
echo "Should return HTTP 200 - and a list of all cards"
echo .
echo .

if [ $pauseFlag -eq 1 ]; then read -p "Press enter to continue..."; fi

# --------------------------------------------------
echo "10) show unconfigured deck"
curl -i -X GET http://localhost:10001/deck --header "Authorization: Bearer kienboec-mtcgToken"
echo "Should return HTTP 200 - and a empty-list"
echo .
curl -i -X GET http://localhost:10001/deck --header "Authorization: Bearer altenhof-mtcgToken"
echo "Should return HTTP 200 - and a empty-list"
echo .
echo .

if [ $pauseFlag -eq 1 ]; then read -p "Press enter to continue..."; fi
