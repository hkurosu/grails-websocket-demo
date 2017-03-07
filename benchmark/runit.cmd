set OPTS=-c 100 -n 1000000 -t 300
set RESULT_FILE=results\results-longrun.json
del /q %RESULT_FILE%
call :RUN stomp.js
for %%T in (websocket, xhr-polling, xhr-streaming) do call :RUN stomp.js --sockjs=%%T
goto END

:RUN
node %* %OPTS% 2>>%RESULT_FILE%
timeout /T 180

:END
