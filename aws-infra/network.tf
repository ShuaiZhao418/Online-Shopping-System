resource "aws_vpc" "main" {
  cidr_block       = var.network_vpc_cidr_block
  instance_tenancy = "default"

  tags = {
    Name = var.network_vpc_name
  }
}


resource "aws_subnet" "public_subnets" {
  count             = var.public_subnet_count
  vpc_id            = aws_vpc.main.id
  cidr_block        = var.public_subnet_cidr_blocks[count.index]
  availability_zone = var.available_timezone[count.index]

  tags = {
    Name = "public_subnet${count.index}"
  }
}

resource "aws_subnet" "private_subnets" {
  count             = var.private_subnet_count
  vpc_id            = aws_vpc.main.id
  cidr_block        = var.private_subnet_cidr_blocks[count.index]
  availability_zone = var.available_timezone[count.index]

  tags = {
    Name = "private_subnet${count.index}"
  }
}
