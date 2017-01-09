@setlocal

rem constants
rem set ECHO=@echo

set MODE=%1

rem set
set RESULT_DIR="%~dp0results"
set TIMESTAMP=%date:~10,4%%date:~4,2%%date:~7,2%-%time:~0,2%%time:~3,2%%time:~6,2%
set RESULT_FILE=%RESULT_DIR%/results-%TIMESTAMP%.json
if not exist %RESULT_DIR% md %RESULT_DIR%

if NOT "%ECHO%" == "" set RESULT_FILE=NUL

if "%MODE%" == "http" goto HTTP
if "%MODE%" == "stomp" goto STOMP
if "%MODE%" == "" (
  call :HTTP
  call :STOMP
) else (
  call :ITERATE %*
)
goto END

:HTTP
call :ITERATE http.js -k
rem only keepAlive for now
rem call :ITERATE http.js
goto END

:STOMP
if "%YARGS_STOMP_SOCKJS_SERVER%" == "" (
    call :ITERATE stomp.js
    for %%T in (websocket, xhr-polling, xhr-streaming) do call :ITERATE stomp.js --sockjs=%%T
) else (
    call :ITERATE stomp.js
)
goto END

:ITERATE
@if "%TIMEOUT%" == "" set TIMEOUT=300
%ECHO% node %* %OPTS% -c 1    -n 1000    -t %TIMEOUT% 2>> %RESULT_FILE%
%ECHO% node %* %OPTS% -c 5    -n 5000    -t %TIMEOUT% 2>> %RESULT_FILE%
%ECHO% node %* %OPTS% -c 10   -n 100000  -t %TIMEOUT% 2>> %RESULT_FILE%
%ECHO% node %* %OPTS% -c 100  -n 100000  -t %TIMEOUT% 2>> %RESULT_FILE%
%ECHO% node %* %OPTS% -c 1000 -n 100000  -t %TIMEOUT% 2>> %RESULT_FILE%

:END

@endlocal