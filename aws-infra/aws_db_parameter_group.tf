resource "aws_db_parameter_group" "example" {
  name        = "example-parameter-group"
  family      = "mysql5.7"
  description = "Example parameter group for MySQL 5.7"

  parameter {
    name  = "max_connections"
    value = "500"
  }

  parameter {
    name  = "log_output"
    value = "FILE"
  }

  tags = {
    Name = "Example Parameter Group"
  }
}
