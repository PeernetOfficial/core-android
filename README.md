# core-android
Testing playground to support the peernet protocol on android phones. The implementation above is a proof of concept android application. 
There is currently no modifications done to the peernet core to get it running on android phones. As a brief the entire project is done using Kotlin 
with Go mobile.

## Disclaimer:
It is important to note that there could be scenarios where the phone could potencially freeze. The current commits are currently unstable. 

## Current Features:
- Upload file to peernet 
   1. Add files to warehouse 
   2. Add file to blockchain
- Download file from peernet 
- View latest files uploaded 

## Build steps/ Installation 
The following steps below demonstrate how to build 
the application:
- Install go mobile 
```
go install golang.org/x/mobile/cmd/gomobile@latest
```
- Initialize go mobile 
```
gomobile init
```
// Todo

## Implementation
- // Todo

## Indentified issue 
The core-android only supports upto Android API level 29.

### Api level 30 issue 
The following is a list of the ways that apps are affected by this change (from Api level 30 onwards):
 - NetworkInterface.getHardwareAddress() returns null for every interface.
 - Apps cannot use the bind() function on NETLINK_ROUTE sockets.
 - The ip command does not return information about interfaces.
 - Apps cannot send RTM_GETLINK messages.
 
source (https://developer.android.com/training/articles/user-data-ids#mac-11-plus)

#### Possible solution 
"
SDK 30 prohibits syscall.NetlinkRIB(syscall.RTM_GETADDR, ...) which Go's net.Interfaces uses. Implement an Android specific version of net.Interfaces to use instead.

Passing primitive types across JNI is relatively straightforward, passing a single object of a complex class is annoying but still possible, but passing lists and other more complex data structures is way harder. As such, this commit added a Java routine to render the interface information to a string and pass that across JNI as a primitive type for Go code to parse.
"
PR with a solution: https://github.com/tailscale/tailscale-android/pull/21 

[Track issue](https://github.com/PeernetOfficial/core/issues/83)





