memory=`ssh -i ~/.ssh/komquests.pem ubuntu@komquests.com './scripts/free_memory.sh'`
echo "komquests memory: ${memory}MB"
