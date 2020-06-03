# TAS NFS Volume Watch Service Example

## Setup

### Create NFS service
```sh
cf create-service nfs Existing SERVICE-INSTANCE-NAME -c '{"share":"<hostname-and-path-to-nfs-volume>", "version":"4.0"}'
```

### Build and Push the App
```sh
./gradlew assemble
cf push -p build/libs/cf-nfs-watchservice-0.0.1-SNAPSHOT.jar --no-start
```

### Bind NFS Service
```sh
cf bind-service cf-nfs-watchservice SERVICE-INSTANCE-NAME -c '{"uid":"<provided>","gid":"<provided>","mount":"/var/watcherdemo"}'
``` 

### Start the App
```sh
cf start cf-nfs-watchservice
```

You should see the following output in the logs upon first startup:
```
   2020-06-03T11:55:29.69-0500 [APP/PROC/WEB/0] OUT 2020-06-03 16:55:29.696  INFO 25 --- [           main] c.e.c.UploadWatcherService               : Upload directory created: /var/watcherdemo/upload
```

## Generate and Watch Uploads

### Generate a Random File
```
curl -X POST https://cf-nfs-watchservice.yourdomain.com/generate
{"generated":true}
```

### Observe the Logs
You should see log output like the following indicating the file was created and a watch event was triggered:
```
2020-06-03T12:00:50.44-0500 [APP/PROC/WEB/0] OUT 2020-06-03 17:00:50.440  INFO 25 --- [nio-8080-exec-8] c.example.cfnfswatchservice.RandomFile   : File created: /var/watcherdemo/upload/092e589a-53aa-4006-9b63-fa2f1f7dd998
2020-06-03T12:00:50.44-0500 [APP/PROC/WEB/0] OUT 2020-06-03 17:00:50.441  INFO 25 --- [       Thread-2] c.e.c.UploadWatcherService               : Event kind: ENTRY_CREATE | File affected: 092e589a-53aa-4006-9b63-fa2f1f7dd998
```

## Observe the Filesystem
### SSH into App Container
```sh
cf ssh cf-nfs-watchservice
```

### Observe File Permissions
The mount point will be owned by `nobody` in the group `root` with permissions `700`:
```sh
$ ls -l /var | grep watcherdemo
drwx------  3 nobody root  255 Jun  3 16:55 watcherdemo
```

The created `upload` directory will be owned by `nobody` in the group `vcap` with permissions `755`:
```sh
$ ls -l /var/watcherdemo
drwxr-xr-x 2 nobody vcap 108 Jun  3 17:03 upload
```
