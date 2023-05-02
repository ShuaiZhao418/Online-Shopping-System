resource "random_string" "aws-s3-bucket-name" {
  length  = 8 
  special = false
}
