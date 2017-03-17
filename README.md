# README #

**SAE_1.0.0_executable**

## Requirements for installation ##
	Before installing SAE, ensure that your environment meets the following minimum requirements:
        * JDK: 1.7.0_07 (http://www.oracle.com)
        * JRE: 1.8.0_51 (http://www.oracle.com) 
        * OS: Windows Seven or higher
        * Eclipse: MARS.1 (https://eclipse.org) with JRE 1.8.0_51 (http://www.oracle.com)
        * jUCMNav: 7.0.0  ( http://jucmnav.softwareengineering.ca/jucmnav)
        * PostgreSQL: 9.0 or higher (https://www.postgresql.org)

*Note:* Before you've installed SAE, in Windows, you must set the  PATH  environment variable to point to the Java installation directory.

To set the PATH variable:
        1) Locate the Java installation directory. If you didn't change the path during installation, it'll be something like C:\Program Files\Java\jdk1.7.0_07\bin
        2) Do one of the following:
		Windows 7 – Right click My Computer and select Properties > Advanced
		Windows 8 – Go to Control Panel > System > Advanced System Settings
        3) Click the Environment Variables button
        4) Under System Variables, click New
        5) In the Variable Name field, enter PATH 
        6) In the Variable Value field, enter your JRE installation path
        7) Click OK and Apply Changes as prompted

You'll need to close and re-open any command windows that were open before you made these changes, as there's no way to reload environment variables from an active command prompt. If the changes don't take effect after reopening the command window, restart Windows.

## Installation and execution guide ##
        1) Add a new site selecting Help > Install New Software
        2) Click the Add... button
        3) Type a name into the Name text box
        4) The software site is in your local file system (including a SAE folder), click Local... to specify the directory location of the site

*Note:* Make sure you have Java 8 running in Eclipse:
        1. Window > Preference > Java  > Installed JREs
        2. Select JRE 1.8.0_51