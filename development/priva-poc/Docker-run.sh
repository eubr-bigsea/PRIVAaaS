docker run  -d                          \
      --name priva                      \
      -v $PWD/logs:/PRIVAaaS/logs       \
      -p 4567:4567                      \
      privaaas/openjdk 
