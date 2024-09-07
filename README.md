# cinematrix
A media-sharing platform with robust streaming capabilities. Media files are stored on a Backblaze S3-compatible cloud storage, cached, and served via the Cloudflare CDN. 

Because of private tokens cannot be shared in github, Cinematrix is forked, with removing all history commits (because the commits contain token).
## Development
This project use a custom-built Java scripting program (jpexec) to run the backend and frontend at the same time for development purposes. The script is private and not meant for public use.
### Windows
To run the project in Windows, you can use the following command:
```cmd
./jpexec.exe "GREEN;[BACKEND];./mvnw.cmd spring-boot:run" "BLUE;[FRONTEND];cmd /c npm run start"
```
You can also execute the cmd file `start.cmd` to run the project. Note that using `CTRL + C` with this method will prompt `Terminate batch job (Y/N)?` as the batch job is still running. This behavior is harmless can be just ignored (either enter `Y`, `N` or just use `CTRL + C` again).
```
./start
```
### Linux and macOS
To run the project in Linux and macOS, you can use the following command:
```sh
java -jar ./jpexec.jar "GREEN;[BACKEND];./mvnw spring-boot:run" "BLUE;[FRONTEND];npm start"
```
