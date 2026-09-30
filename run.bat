@echo off
title BluePaw Vet & Grooming Launcher - Pro Edition (FlatLaf)
color 0B

echo.
echo    / \__
echo   (    @\___
echo   /         O
echo  /   (_____/
echo /_____/   U
echo =======================================================
echo    B L U E P A W   V E T   &   G R O O M I N G
echo =======================================================
echo.
echo [1/3] Menginisialisasi sistem...
ping localhost -n 2 >nul

echo [2/3] Memeriksa library (FlatLaf & FlatLaf Extras) dan mengkompilasi file Java...
:: Parameter classpath (-cp) menyertakan library FlatLaf, FlatLaf Extras, & JSVG di folder lib\
javac -cp ".;src/main/resources;lib/*" *.java

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
echo Menjalankan GUI BluePaw Vet & Grooming (Modern FlatLaf UI)...
echo =======================================================
ping localhost -n 2 >nul

:: Jalankan program dengan menyertakan folder lib/ dan resources di classpath
java -cp ".;src/main/resources;lib/*" PetCareGUI

echo.
echo =======================================================
echo [!] BluePaw Vet & Grooming telah ditutup. Terima kasih!
echo =======================================================
pause
