; xpontus.nsi

; MODERN UI
!include "MUI.nsh"   ; You want to have a fancy setup, don't you?
!define xpontusVersion "1.0.0RC1" ;

Name "XPontus XML Editor"         ; Name (e.g. in title of window)
OutFile "xpontus-${xpontusVersion}.exe"  ; The setup exe filename
SetCompressor LZMA   ; We will use LZMA for best compression

;Default installation folder
InstallDir "$PROGRAMFILES\xpontus"
  
;Get installation folder from registry if available

; INTERFACE SETTINGS 
; The MODERN UI needs this 
!define MUI_HEADERIMAGE
!define MUI_HEADERIMAGE_BITMAP "${NSISDIR}/Contrib/Graphics/Header/nsis.bmp" ; optional
!define MUI_ABORTWARNING

; PAGES
; The pages the setup will have.
!insertmacro MUI_PAGE_LICENSE "license.txt"
!insertmacro MUI_PAGE_COMPONENTS
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES
  
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES

;Languages
!insertmacro MUI_LANGUAGE "English"
!insertmacro MUI_LANGUAGE "Catalan"
!insertmacro MUI_LANGUAGE "Spanish"
!insertmacro MUI_LANGUAGE "French"

; SECTIONS
Section "The program" "XPontus XML Editor program"
SectionIn RO
SetOutPath "$INSTDIR"
File "license.txt" ; Point it to a file which exists 
File "Readme.txt" 
File "xpontus.exe" 
File /r "lib"
SectionEnd

Section "The user guide" "XPontus XML Editor user guide"
SetOutPath "$INSTDIR\documentation"
File /r "docs\guide"
SectionEnd

Section "The API documentation" "Javadoc documentation"
SetOutPath "$INSTDIR\documentation"
File /r "docs\javadoc"
SectionEnd

Section "The sources" "The source code"
SetOutPath "$INSTDIR\src"
File /r "..\..\src"
SectionEnd

Section "" Main
CreateDirectory "$SMPROGRAMS\xpontus"
CreateShortCut "$SMPROGRAMS\xpontus\xpontus.lnk"  "$INSTDIR\xpontus.exe" 
CreateShortCut "$SMPROGRAMS\xpontus\Uninstall.lnk"  "$INSTDIR\Uninstall.exe"
; Uninstaller
 WriteUninstaller $INSTDIR\Uninstall.exe
 WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\XPontus" "DisplayName"\
"XPontus XML Editor (remove only)"

WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\XPontus" "UninstallString" \
"$INSTDIR\Uninstall.exe"
SectionEnd

Section "Uninstall"
RMDir /r "$INSTDIR\xpontus"    ; removes the installation directory and files recursively.
RMDIR /r "$SMPROGRAMS\xpontus" ; removes the start menu directory and files recursively.
DeleteRegKey HKEY_LOCAL_MACHINE "SOFTWARE\XPontus"
DeleteRegKey HKEY_LOCAL_MACHINE "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\XPontus"
SectionEnd
