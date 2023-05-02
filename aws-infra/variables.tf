variable "provider_profile" {
  description = "AWS Provider Profile"
  type        = string
  default     = "dev"
}

variable "ami_id" {
  description = "ami for create ec2 instance"
  type        = string
  default     = ""

variable "network_vpc_name" {
  description = "AWS VPC Name"
  type        = string
  default     = "my-example-vpc1"
}

variable "network_vpc_cidr_block" {
  description = "AWS VPC CIDR"
  type        = string
  default     = "10.10.0.0/16"
}

variable "network_gw_name" {
  description = "AWS VPC Name"
  type        = string
  default     = "my-internet-gateway"
}

variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "us-west-1"
}

variable "public_subnet_count" {
  description = "Number of public subnets."
  type        = number
  default     = 3
}

variable "private_subnet_count" {
  description = "Number of private subnets."
  type        = number
  default     = 3
}

variable "available_timezone" {
  description = "Available time zone subnets."
  type        = list(string)
  default = [
    "us-west-2a",
    "us-west-2b",
    "us-west-2c",
  ]
}

variable "public_subnet_cidr_blocks" {
  description = "Available cidr blocks for public subnets."
  type        = list(string)
  default = [
    "10.10.1.0/24",
    "10.10.2.0/24",
    "10.10.3.0/24",
  ]
}

variable "private_subnet_cidr_blocks" {
  description = "Available cidr blocks for private subnets."
  type        = list(string)
  default = [
    "10.10.4.0/24",
    "10.10.5.0/24",
    "10.10.6.0/24",
  ]
}

variable "public_route_table_name" {
  description = "AWS Public Route Table"
  type        = string
  default     = "my-public-route-table"
}

variable "private_route_table_name" {
  description = "AWS Private Route Table"
  type        = string
  default     = "my-private-route-table"
}

variable "aws_route_table_gw_associate_cidr_block" {
  description = "AWS Private Route Table"
  type        = string
  default     = "0.0.0.0/0"
}

variable "aws_key_pair_key" {
  description = "AWS key pair key"
  type        = string
  default     = ""
}

variable "aws_key_pair_name" {
  description = "AWS key pair name"
  type        = string
  default     = "aws-dev-us-west-2"
}

variable "aws_s3_bucket_region" {
  description = "AWS s3 bucket region"
  type        = string
  default     = "us-west-2"
}

variable "aws_iam_role_name" {
  description = "aws_iam_role_name"
  type        = string
  default     = "EC2-CSYE6225"
}

variable "aws_iam_policy_name" {
  description = "aws_iam_policy_name"
  type        = string
  default     = "WebAppS3"
}

variable "aws_cloudwatch_policy_name" {
  description = "aws_cloudwatch_policy_name"
  type        = string
  default     = "CloudWatchAgentServerPolicy"
}

variable "aws_route_zone_id" {
  description = "aws_route_zone_id"
  type        = string
  default     = "Z0823859BOJVG2X8S2EG"
}

variable "aws_route_zone_name" {
  description = "aws_route_zone_name"
  type        = string
  default     = "dev.shuaizhao.me"
}

variable "aws_kms_accountId" {
  description = "aws kms account id"
  type        = string
  default     = ""
}


