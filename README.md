<h1 align="center">
  <br>
	<h1>Core Android</h1>
  <br>
</h1>

![image](https://user-images.githubusercontent.com/31743758/148452430-a96bbc78-bcfe-4b6b-950a-f4257e63c426.png)

## Topics
1. [Introduciton](#introduction)
2. [Disclaimer](#disclaimer)
3. [Current features](#current-features)
4. [Build from gomobile](#build-from-gomobile)
5. [Implementation](#implementation)
6. [Indentified issues](#indentified-issues)
7. [Potential fixes/improvements](#potential-fixesimprovements) 

<br>

## Introduction
Testing playground to support the peernet protocol on android phones. The implementation above is a proof of concept android application. 
There is currently no modifications done to the peernet core to get it running on android phones. As a brief the entire project is done using Kotlin 
with Go mobile.

### Screenshots:
![image](https://user-images.githubusercontent.com/31743758/148444431-3cb045a3-4950-4f57-9148-84756f6cfcf6.png)

<br>

## Disclaimer:
It is important to note that there could be scenarios where the phone could potencially freeze. The current commits are currently unstable. 

<br>

## Current Features:
1. User perpective:
   - Upload file to peernet 
     1. Add files to warehouse 
     2. Add file to blockchain
   - Download file from peernet 
   - View latest files uploaded 
2. Developers perpective
   - Full support of peernet core (till Api level 29)
   - Ability to interact with peernet core via API calls from peernet server running inside the phone. 

<br>

## Build from gomobile: 
The following steps below demonstrate how to build 
the application:
- Create go project with the package called mobile
```
## The following applies for linux 
- mkdir mobile 
- cd mobile/
- go mod init <module name>
- touch mobile.go
```
- add to ```mobile.go```
```go
package mobile

import (
	"fmt"
	"github.com/PeernetOfficial/core"
	"github.com/PeernetOfficial/core/webapi"
	"github.com/google/uuid"
	"net/http"
	"time"
)

// MobileMain The following function is called as a bind function
// from the Kotlin implementation
func MobileMain(path string) {

	var config core.Config

	// Load the config file
	core.LoadConfig(path+"Config.yaml", &config)

	//Setting modified paths in the config file
	config.SearchIndex = path + "data/search_Index/"
	config.BlockchainGlobal = path + "data/blockchain/"
	config.BlockchainMain = path + "data/blockchain_main/"
	config.WarehouseMain = path + "data/warehouse/"
	config.GeoIPDatabase = path + "data/GeoLite2-City.mmdb"
	config.LogFile = path + "data/log.txt"
    
	// save modified config changes
	core.SaveConfig(path+"Config.yaml", &config)

	backendInit, status, err := core.Init("Your application/1.0", path+"Config.yaml", nil, nil)
	if status != core.ExitSuccess {
		fmt.Printf("Error %d initializing config: %s\n", status, err.Error())
		return
	}

	// start config api server
	webapi.Start(backendInit, []string{"127.0.0.1:5125"}, false, "", "", 10*time.Second, 10*time.Second, uuid.Nil)

	backendInit.Connect()

	// Checks if the go code can access the internet
	if !connected() {
		fmt.Print("Not connected to the internet ")
	} else {
		fmt.Print("Connected")
	}

}

func connected() (ok bool) {
	_, err := http.Get("http://clients3.google.com/generate_204")
	if err != nil {
		return false
	}
	return true
}
```
- Install go mobile 
```
go install golang.org/x/mobile/cmd/gomobile@latest
```
- Initialize go mobile 
```
gomobile init
```
- Add path for Android NDK 
```
export ANDROID_HOME=$HOME/Android/Sdk
```
- Generate .aar and .jar file 
```
gomobile bind -target android .

Output:
mobile.aar  mobile-sources.jar
```
- clone the following repo
```
git clone https://github.com/PeernetOfficial/core-android
```
- add the following files(i.e mobile.aar,mobile-sources.jar) to the following path. Overwrite it if the file already exists 
```
/<path of the repo>/core-android/app/libs/
```
- Open the project in android studio and click the play button. 
![image](https://user-images.githubusercontent.com/31743758/148443246-79ce16c3-d2a0-483a-9396-620deafda6ee.png)

- Before opening the app ensure internal storage is enabled.

 ![image](https://user-images.githubusercontent.com/31743758/148446496-a56f6da9-a484-45cd-88be-4791c58c54a1.png)

<br>

## Implementation 
The following is built using Kotlin, gomobile. Go code is the peernet core protocol. The Kotlin interacts with the with generated .aar (i.e which is the output when compiling using gomobile). The .aar is the peernet core protocol implementation. The interactions with the Go code are the following:
- Call the exported function ```MobileMain(<android application storage path>)```. This is the only function binded to the koltin code. The application path 
  is dervired from the kotlin code. 
- On calling ```MobileMain(<android application storage path>)``` peernet core is started and the webapi is started as well. The api address is the following 
  ```http://127.0.0.1:5125```. 
- The kotlin code only interacts with api calls from the following steps.

Note: The gomobile core only currently runs the foreground when the application is open. 


### Permissions manually needed to be enabled
- Internal storage 
### Android manifest information 
- android.permission.READ_EXTERNAL_STORAGE
- android.permission.WRITE_EXTERNAL_STORAGE
- android.permission.INTERNET
- android.permission.ACCESS_NETWORK_STATE
- android.permission.ACCESS_WIFI_STATE
- Due to http traffic:
  1. android:usesCleartextTraffic="true"

## Indentified issues 
The core-android only supports upto Android API level 29.

### Api level 30 issue 
The following is a list of the ways that apps are affected by this change (from Api level 30 onwards):
 - NetworkInterface.getHardwareAddress() returns null for every interface.
 - Apps cannot use the bind() function on NETLINK_ROUTE sockets.
 - The ip command does not return information about interfaces.
 - Apps cannot send RTM_GETLINK messages.
 
source (https://developer.android.com/training/articles/user-data-ids#mac-11-plus)

<br>

#### Possible solution 
"
SDK 30 prohibits syscall.NetlinkRIB(syscall.RTM_GETADDR, ...) which Go's net.Interfaces uses. Implement an Android specific version of net.Interfaces to use instead.

Passing primitive types across JNI is relatively straightforward, passing a single object of a complex class is annoying but still possible, but passing lists and other more complex data structures is way harder. As such, this commit added a Java routine to render the interface information to a string and pass that across JNI as a primitive type for Go code to parse.
"
PR with a solution: https://github.com/tailscale/tailscale-android/pull/21 

[Track issue](https://github.com/PeernetOfficial/core/issues/83)

## Potential fixes/improvements 
- Providing compatability to API level 30.
- Cleaning up android core code for lesser redundency and readability.
- Running the core as a background process.
- UI improvements for ease of use 
- Displaying downloaded files in the app itself 





