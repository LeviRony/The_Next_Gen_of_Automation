# playwright_project

## Running Codegen

Use the codegen command to run the test generator, followed by the URL of the website you want to generate tests for.
The URL is optional, and you can always run the command without it and then add the URL directly into the browser window
instead.

### Installing Codegen

```
 mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install" 
```

### Running Codegen

```
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="codegen demo.playwright.dev/todomvc"```
```

## Opening Trace Viewer

You can open a saved trace using either the Playwright CLI or in the browser at trace.playwright.dev. Make sure to add
the full path to where your trace.zip file is located.

``` 
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="show-trace trace.zip"
```