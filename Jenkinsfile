// 需要在jenkins的Credentials设置中配置jenkins-harbor-creds、jenkins-k8s-config参数
pipeline {
    agent any
    environment {
        DOCKER_CREDS = credentials('jenkins-docker-creds')
        K8S_CONFIG = credentials('jenkins-k8s-config')
        GIT_TAG = sh(returnStdout: true,script: 'git describe --tags --always').trim()
    }
    parameters {
        string(name: 'DOCKER_IMAGE', defaultValue: 'XXXXXXXXXX', description: 'docker镜像名')
        string(name: 'APP_NAME', defaultValue: 'renren-fast', description: 'k8s中标签名')
        string(name: 'K8S_NAMESPACE', defaultValue: 'demo', description: 'k8s的namespace名称')
        string(name: 'DOCEKR_CREDS_USR', defaultValue: 'XXXX', description: 'docker.hub用户名')
        string(name: 'DOCKER_CREDS_PSW', defaultValue: 'XXX', description: 'docker.hub密码')
    }
    stages {
        stage('Maven Build') {
            when { expression { env.GIT_TAG != null } }
            agent any
            steps {
                sh 'mvn clean package -Dfile.encoding=UTF-8 -DskipTests=true'
                stash includes: 'target/*.jar', name: 'app'
            }

        }
        stage('Docker Build') {
            when { 
                allOf {
                    expression { env.GIT_TAG != null }
                }
            }
            agent any
            steps {
                unstash 'app'
                sh "docker login -u ${DOCEKR_CREDS_USR} -p ${DOCKER_CREDS_PSW}  https://hub.docker.com"
                sh "docker build --build-arg JAR_FILE=`ls target/*.jar |cut -d '/' -f2` -t ${params.DOCKER_IMAGE}:${GIT_TAG} ."
                sh "docker push ${params.DOCKER_IMAGE}:${GIT_TAG}"
                sh "docker rmi ${params.DOCKER_IMAGE}:${GIT_TAG}"
            }
            
        }
        stage('Deploy') {
            when { 
                allOf {
                    expression { env.GIT_TAG != null }
                }
            }
            agent {
                docker {
                    image 'lwolf/helm-kubectl-docker'
                }
            }
            steps {
                sh "echo whoami"
                // sh "sudo mkdir -p ~/.kube"
                // sh "echo ${K8S_CONFIG} | base64 -d > ~/.kube/config"
                sh "sed -e 's#{IMAGE_URL}#${params.DOCKER_IMAGE}#g;s#{IMAGE_TAG}#${GIT_TAG}#g;s#{APP_NAME}#${params.APP_NAME}#g;s#{SPRING_PROFILE}#k8s-test#g' k8s-deployment.tpl > k8s-deployment.yml"
                sh "cat k8s-deployment.yml"
                sh "kubectl apply -f k8s-deployment.yml --namespace=${params.K8S_NAMESPACE}"
            }
            
        }
        
    }
}
