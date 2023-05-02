resource "aws_db_subnet_group" "example" {
  name        = "example-subnet-group"
  description = "Example DB subnet group"
  subnet_ids  = [aws_subnet.private_subnets[0].id, aws_subnet.private_subnets[1].id, aws_subnet.private_subnets[2].id]
}
