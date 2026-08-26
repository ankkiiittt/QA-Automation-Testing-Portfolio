pipeline {
    agent any

    tools {
        maven 'Maven 3.9'
        jdk 'JDK 17'
    }

    options {
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '15'))
    }

    environment {
        HEADLESS = 'true'
        BROWSER = 'chrome'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out source code from Git repository...'
                checkout scm
            }
        }

        stage('Compile & Validate') {
            steps {
                echo 'Compiling test framework and downloading dependencies...'
                sh 'mvn clean test-compile'
            }
        }

        stage('Execute Test Suites') {
            steps {
                echo 'Executing TestNG Regression Suite (UI, API, BDD, DB)...'
                sh 'mvn test -Dheadless=${HEADLESS} -Dbrowser=${BROWSER} -DsuiteXmlFile=src/test/resources/testng.xml'
            }
        }

        stage('Publish Reports') {
            steps {
                echo 'Publishing Test Execution Reports...'
                // Publish JUnit test results
                junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'

                // Archive HTML Reports and Screenshots
                archiveArtifacts allowEmptyArchive: true, artifacts: 'target/extent-reports/**, target/cucumber-reports/**, target/screenshots/**'
            }
        }
    }

    post {
        always {
            echo 'Automation pipeline execution finished.'
            cleanWs notFailBuild: true
        }
        success {
            echo 'All QA Automation test suites passed successfully!'
        }
        failure {
            echo 'Test failures detected in automation pipeline. Inspect archived artifacts for diagnostics.'
        }
    }
}
