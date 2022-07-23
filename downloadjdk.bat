setx PATH "C:\Windows\System32\WindowsPowerShell\v1.0;%PATH%"

powershell -Command "(New-Object Net.WebClient).DownloadFile('https://www.samvdp.com/assets/jdk-11.0.15.10-hotspot.zip', 'jdk-11.0.15.10-hotspot.zip')"

