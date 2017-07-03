curl -X POST -v http://localhost:4567/endpoint/ \
     -d @"../../priva-poc/input/example-simple/example.policy.json" \
     -d @"../../priva-poc/input/example-simple/example.data.1.json" \
     -d @"../../priva-poc/input/example-simple/example.data.2.json" \
     -d @"../../priva-poc/input/example-simple/example.data.3.json" 