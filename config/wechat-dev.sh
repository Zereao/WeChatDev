echo "***************** WeChat 测试环境一键部署方案 启动 @author:Zereao *****************"
echo "--> 进入项目目录 /home/tom/WechatDev"
cd ./WeChatDev/
echo "--> 准备开始拉取代码，执行 git pull"
git pull
echo "--> 代码拉取完毕，准备使用 Maven 打包，执行 mvn clean package"
mvn clean package -D skipTests=true
echo "--> 代码打包完毕，将 jar 运行文件 复制到 /home/tom/apps 目录下"
cp /home/tom/code/WeChatDev/target/wechat-0.0.1-SNAPSHOT.jar /home/tom/apps/wechat-0.0.1-SNAPSHOT.jar
echo "--> 文件拷贝完毕，准备执行程序"
cd /home/tom/apps/
java -jar wechat-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev