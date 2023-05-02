resource "aws_iam_policy" "example" {
  name        = var.aws_iam_policy_name
  policy      = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action   = [
          "s3:PutObject",
          "s3:DeleteObject"
        ]

        Effect   = "Allow"
        Resource = [
          "arn:aws:s3:::${aws_s3_bucket.my_bucket.id}",
          "arn:aws:s3:::${aws_s3_bucket.my_bucket.id}/*"
        ]
      }
    ]
  })
}

