# Mobile Module


## Running
Device Platform ("Dplatform") options: android, ios
Device Environment("DrunEnv") options:  local, bStack, sLabs
Android local:
```bash
mvn -pl mobile -am test \
  -Dplatform=android \
  -DrunEnv=local \
  -DappiumUrl=http://127.0.0.1:4723/ \
  -Dcap.deviceName="Pixel_7_API_34" \
  -Dcap.app="/Users/you/apps/demo.apk"
```

iOS local:
```bash
mvn -pl mobile -am test \
  -Dplatform=ios -DrunEnv=local \
  -Dcap.deviceName="iPhone 15" \
  -Dcap.app="/Users/you/apps/Demo.app"
```

BrowserStack:
```bash
export BROWSERSTACK_USERNAME=...; export BROWSERSTACK_ACCESS_KEY=...
mvn -pl mobile -am test -Dplatform=android -DrunEnv=bs \
  -Dcap."bstack:options".userName=$BROWSERSTACK_USERNAME \
  -Dcap."bstack:options".accessKey=$BROWSERSTACK_ACCESS_KEY
```