# Online-Shopping-System
A web application based on Spring Boot and AWS.


## Summary:    
- Developed a scalable and autorun online shopping platform using Spring Boot and AWS, integrated CI/CD with Github Action.
- Automated network setup and resource creation using Cloud Formation [Terraform](https://www.terraform.io/), AWS CLI, and shell scripts. 
- Deployed the web application to a custom AWS EC2, built on an optimized [AMI](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/AMIs.html) using Packer.
- Persisted user, commodity data on AWS RDS and images on AWS S3, decreasing data retrieval time and improving data security.
- Developed a serverless password reset function using Lambda, SES and SNS, reducing the account password reset problems.
- Implemented [ELB-based](https://aws.amazon.com/cn/elasticloadbalancing/) autoscaling service to handle numerous HTTP requests and responses. 
- Configured AWS WAF and Route 53 to improve system security and compliance.
- Used SystemD to autorun the application in AWS EC2 instance.
- Used SLF4J and CloudWatch Logs for logging, created application counter Metrics with StatsD.

## Tools and Technologies

|Function|Tools|
|-------|-------|
|Infrastructure|VPC, EC2, S3, RDS, ELB, Route53, Terraform, Packer|
|Webapp|Java, Spring Boot, Maven|
|CI/CD|AWS Code Deploy, Github Action|
|Logging and alerting|Cloud Watch, SNS, SES, Lambda|
|Security|WAF, KMS|

## Application Architecture

![Architecture Diagram](https://github.com/ShuaiZhao418/Online-Shopping-System/blob/main/Application%20Architecture.png)

