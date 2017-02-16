# Volcano
A mockdata generator

# Feature
1. Huge flow from a single machine.
2. Stable flow rate that match the configuration.
3. Configurable for mock data content.
  > Number or string fields can be generated according to the config randomly.

# Usage
  ```
    volcano path/to/your/config
  ```

# Configuration
## Send to Kafka
  ```
  template: "Oct 31 00:24:53 haproxy-1 haproxy[30091]: 192.168.1.1:66666 [31/Oct/2013:00:24:53.337] example.volcano.org lua_pool/lua_web2 0/0/0/11/44 {http_code} {bytes_sent} - - ---- 230/230/96/22/0 0/0 {method} /foo/bar?a=x&b=y HTTP/1.1"
  mapping: "http_code:string:200,404,502;bytes_sent:float:10,5000;method:string:GET,POST"
  duration: 100
  rate: 500
  kafkaTopic: "mock-data-test"
  kafkaBrokers: "192.168.10.10:9092"
  kafkaClientId: "none"
  ```

## Send to a local file
  ```
  Coming soon!
  ```
