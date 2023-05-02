# resource "aws_route53_record" "example_a_record" {
#   zone_id = "${var.aws_route_zone_id}"
#   name    = "${var.aws_route_zone_name}"
#   type    = "A"
#   ttl     = "60"
#   records = ["${aws_launch_template.lt.public_ip}"]
# }

resource "aws_route53_record" "example_a_record" {
  zone_id = "${var.aws_route_zone_id}"
  name    = "${var.aws_route_zone_name}"
  type    = "A"
  
  alias {
    name                   = aws_lb.lb.dns_name
    zone_id                = aws_lb.lb.zone_id
    evaluate_target_health = true
  }
}