if kubectl apply -f k8s/deployment.yml | grep -q unchanged; then
  echo "=> Patching k8s/deployment to forece update"
  kubectl patch -f k8s/deployment.yml -p "{\"spec\":{\"template\":{\"metadata\":{\"annotations\":{\"ci-last-updated\": \"`date +'%s'`\"}}}}}"
else
 echo "=> k8s/deployment apply has changed the object, no need to force image update."
fi

kubectl rollout status -f k8s/deployment.yml