mvn clean install
docker build -t springboot-sns .
kubectl run springboot-sns --image=springboot-sns --image-pull-policy=IfNotPresent
kubectl expose pod springboot-sns --type=LoadBalancer --name=springboot-sns-service --port=8201
aws cloudformation create-stack --stack-name "springboot-sns-without-sqs" \                                                                             SIGINT(2) ↵  3783  16:52:41
--endpoint-url=http://localhost:4566 --profile=localstack \
--template-body file:////Users/shobhitmathur/Code/personal/dynamo-db-local-springboot/sns-stack-springboot-without-sqs.yml
kubectl logs -f springboot-sns