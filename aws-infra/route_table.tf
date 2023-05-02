resource "aws_route_table" "public_route_table" {
  vpc_id = aws_vpc.main.id

  route {
    cidr_block = var.aws_route_table_gw_associate_cidr_block
    gateway_id = aws_internet_gateway.gw.id
  }

  tags = {
    Name = var.public_route_table_name
  }
}

resource "aws_route_table" "private_route_table" {
  vpc_id = aws_vpc.main.id

  tags = {
    Name = var.private_route_table_name
  }
}

resource "aws_route_table_association" "public_associations" {
  count          = var.public_subnet_count
  subnet_id      = aws_subnet.public_subnets[count.index].id
  route_table_id = aws_route_table.public_route_table.id
}

resource "aws_route_table_association" "private_associations" {
  count          = var.public_subnet_count
  subnet_id      = aws_subnet.private_subnets[count.index].id
  route_table_id = aws_route_table.private_route_table.id
}