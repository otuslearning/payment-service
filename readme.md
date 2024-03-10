# Instruction
1. Build docker image
    ```shell
    docker buildx build -t payment-service:0.0.1 .
    ```
2. Add tag to image
    ```shell
    docker tag payment-service:0.0.1 otuslearning/payment-service:0.0.1
    ```
3. Push image
    ```shell
    docker push otuslearning/payment-service:0.0.1
    ```