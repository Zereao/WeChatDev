echo "***************** 脚本开始执行..... *****************"
echo "[ git add . ] 准备执行....."
git add .
echo "[ git add . ] 执行完毕....."
echo "[ git commit -m\"MESSAGE\" ] 准备执行....."
read -p "Please enter the commit Message：" message
git commit -m"$message"
echo "[ git commit -m\"$message\" ] 执行完毕....."
echo "[ git pull ] 准备执行....."
git pull
echo "[ git pull ] 执行完毕....."
echo "[ git push ] 准备执行....."
git push
echo "[ git push ] 执行完毕....."
echo "***************** 脚本执行完毕..... *****************"