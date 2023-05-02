# config encryption
resource "aws_s3_bucket" "my_bucket" {
  bucket = "awss3${lower(random_string.aws-s3-bucket-name.id)}"
  acl    = "private"
  
  // force the s3 to be delete even if it is not empty
  force_destroy = true

  server_side_encryption_configuration {
    rule {
      apply_server_side_encryption_by_default {
        sse_algorithm = "AES256"
      }
    }
  }
  # config the acl/lifecycle
  lifecycle_rule {
    id      = "log"
    enabled = true

    prefix = "log/"

    tags = {
      rule      = "log"
      autoclean = "true"
    }

    transition {
      days          = 30
      storage_class = "STANDARD_IA"
    }
  }
}
# config public access setting
resource "aws_s3_bucket_public_access_block" "example" {
  bucket = aws_s3_bucket.my_bucket.id

  block_public_acls       = true
  block_public_policy     = true
  ignore_public_acls      = true
  restrict_public_buckets = true
}
