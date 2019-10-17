FastDFS基本操作
============== 
## 引用
    * clone该项目
    
    * 执行：mvn clean install -Dmaven.test.skip 打成jar包
    
    * 直接引用：
        <dependency>
            <groupId>fastdfs-api</groupId>
            <artifactId>fastdfs-api</artifactId>
            <version>1.2</version>
        </dependency>
    
## 用法
    * 复制项目中的文件`fastdfs.properties`到自己的项目中
    
    * 新建一个配置类
        @Component
        @RefreshScope
        @Configuration
        @ConfigurationProperties(prefix = "fastdfs")
        public class FastDFSBeanConfig {

            @Value("${es_file_server_addr}")
            private String fileServerAdder;

            @Value("${es_max_storage_connection}")
            private String maxStorageConnection;

            @Value("${es_flag_file_name}")
            private String flagFileName;

            @Value("${es_flag_file_suffix}")
            private String flagFileSuffix;

            @Value("${es_max_file_size}")
            private String maxFileSize;

            @Value("${es_minIdle}")
            private String minIdle;

            /**
             * fastDFS Properties
             */
            private String charset;
            private String httpAntiStealToken;
            private String httpSecretKey;
            private String httpTrackerHttpPort;
            private String trackerServers;
            private String connectTimeoutInSeconds;
            private String networkTimeoutInSeconds;

            @Bean
            public FastDFSBean fastDFSBean(){
                FastDFSBean fastDFSBean = new FastDFSBean();
                fastDFSBean.setMaxFileSize(Long.valueOf(maxFileSize));
                fastDFSBean.setMaxStorageConnection(Integer.valueOf(maxStorageConnection));
                fastDFSBean.setFlagFileName(flagFileName);
                fastDFSBean.setMinIdle(Integer.valueOf(minIdle));
                fastDFSBean.setHttpSecretKey(httpSecretKey);
                fastDFSBean.setFlagFileSuffix(flagFileSuffix);
                fastDFSBean.setFileServerAdder(fileServerAdder);

                Properties properties = new Properties();
                properties.setProperty(ClientGlobal.PROP_KEY_CHARSET,getCharset());
                properties.setProperty(ClientGlobal.PROP_KEY_HTTP_SECRET_KEY,getHttpSecretKey());
                properties.setProperty(ClientGlobal.PROP_KEY_CONNECT_TIMEOUT_IN_SECONDS,getConnectTimeoutInSeconds());
                properties.setProperty(ClientGlobal.PROP_KEY_HTTP_ANTI_STEAL_TOKEN,getHttpAntiStealToken());
                properties.setProperty(ClientGlobal.PROP_KEY_HTTP_TRACKER_HTTP_PORT,getHttpTrackerHttpPort());
                properties.setProperty(ClientGlobal.PROP_KEY_TRACKER_SERVERS,getTrackerServers());
                fastDFSBean.setProperties(properties);
                return fastDFSBean;
            }

            @Bean
            public FastDFSClient fastDFSClient(FastDFSBean fastDFSBean){
                return new FastDFSClient(fastDFSBean);
            }

            @Bean
            public FastDFSDefault fastDFSDefault(FastDFSClient fastDFSClient){
                return new FastDFSDefaultImp(fastDFSClient);
            }

            public String getCharset() {
                return charset;
            }

            public void setCharset(String charset) {
                this.charset = charset;
            }

            public String getHttpAntiStealToken() {
                return httpAntiStealToken;
            }

            public void setHttpAntiStealToken(String httpAntiStealToken) {
                this.httpAntiStealToken = httpAntiStealToken;
            }

            public String getHttpSecretKey() {
                return httpSecretKey;
            }

            public void setHttpSecretKey(String httpSecretKey) {
                this.httpSecretKey = httpSecretKey;
            }

            public String getHttpTrackerHttpPort() {
                return httpTrackerHttpPort;
            }

            public void setHttpTrackerHttpPort(String httpTrackerHttpPort) {
                this.httpTrackerHttpPort = httpTrackerHttpPort;
            }

            public String getTrackerServers() {
                return trackerServers;
            }

            public void setTrackerServers(String trackerServers) {
                this.trackerServers = trackerServers;
            }

            public String getConnectTimeoutInSeconds() {
                return connectTimeoutInSeconds;
            }

            public void setConnectTimeoutInSeconds(String connectTimeoutInSeconds) {
                this.connectTimeoutInSeconds = connectTimeoutInSeconds;
            }

            public String getNetworkTimeoutInSeconds() {
                return networkTimeoutInSeconds;
            }

            public void setNetworkTimeoutInSeconds(String networkTimeoutInSeconds) {
                this.networkTimeoutInSeconds = networkTimeoutInSeconds;
            }
        }
        
    * 实例化FastDFSDefault
        @Autowired
        private FastDFSDefault fastDFSDefault;
        
    * 文件上传
        @RequestMapping(value = "/common/upload")
        public String upload(){
            File file = new File("F:\\PIC\\images\\20180605185415_YKhlw.jpeg");
            FastDFSResponse fastDFSResponse = fastDFSDefault.upload(file,"F:\\PIC\\images\\20180605185415_YKhlw.jpeg", new HashMap<>());
            return JSON.toJSONString(fastDFSResponse);
        }
        
    * 文件下载
        @RequestMapping(value = "/common/download")
        public String download(@RequestParam String fp){
            FastDFSResponse fastDFSResponse = fastDFSDefault.download(fp,"C:\\Users\\Shinelon\\Desktop\\");
            return JSON.toJSONString(fastDFSResponse);
        }
        
    * 文件删除
        @RequestMapping(value = "/common/delete")
        public String delete(@RequestParam String fp){
            FastDFSResponse fastDFSResponse = fastDFSDefault.delete(fp);
            return JSON.toJSONString(fastDFSResponse);
        }
## 具体例子
[fastDFS-examples中的fastDFSProducer](https://github.com/XiFYuW/fastDFS-examples/tree/master/fastDFSProducer)

## FastDFS结合FastDHT服务搭建访问百度即可
