# Performance Module

```bash
$ cd /performance/

mvn -q clean verify \
  -Dthreads=5 -Dduration=60 -DrampUp=10 \
  -DbaseUrl=https://practice.expandtesting.com
```