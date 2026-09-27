@echo off
title PetCare System Launcher - Pro Edition (FlatLaf)
color 0B

echo.
echo    / \__
echo   (    @\___
echo   /         O
echo  /   (_____/
echo /_____/   U
echo =======================================================
echo          P E T C A R E   S Y S T E M   L A U N C H E R
echo =======================================================
echo.
echo [1/3] Menginisialisasi sistem...
ping localhost -n 2 >nul

echo [2/3] Memeriksa library (FlatLaf) dan mengkompilasi file Java...
:: Tambahkan parameter classpath (-cp) agar bisa membaca library FlatLaf di folder lib\
javac -cp ".;lib/*" *.java

if %ERRORLEVEL% neq 0 (
    color 0C
    echo.
    echo =======================================================
    echo [X] Kompilasi GAGAL! Silakan periksa pesan error di atas.
    echo =======================================================
    pause
    exit /b %ERRORLEVEL%
)

echo [3/3] Kompilasi BERHASIL! 
echo.
echo Menjalankan GUI PetCare System (Modern UI)...
echo =======================================================
ping localhost -n 2 >nul

:: Jalankan program dengan menyertakan folder lib/ di classpath
java -cp ".;lib/*" PetCareGUI

echo.
echo =======================================================
echo [!] PetCare System telah ditutup. Terima kasih!
echo =======================================================
pause
