resource "aws_key_pair" "deployer" {
  key_name   = var.aws_key_pair_name
  public_key = var.aws_key_pair_key
}

