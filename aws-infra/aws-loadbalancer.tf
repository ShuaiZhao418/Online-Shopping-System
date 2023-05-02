resource "aws_lb" "lb" {
    name = "csye6225-lb"
    internal = false
    load_balancer_type = "application"
    // attach security group
    security_groups = [aws_security_group.loadbalancer.id]
    subnets = [aws_subnet.public_subnets[0].id, aws_subnet.public_subnets[1].id, aws_subnet.public_subnets[2].id]

    tags = {
        Application = "WebApp"
    }
}

resource "aws_lb_listener" "example" {
    load_balancer_arn = aws_lb.lb.arn
    port = 443
    protocol = "HTTPS"
    ssl_policy        = "ELBSecurityPolicy-2016-08"
    certificate_arn   = ""

    default_action {
        type = "forward"
        target_group_arn = aws_lb_target_group.alb_tg.arn
    }
}

resource "aws_lb_target_group" "alb_tg" {
    name = "csye6225-lb-alb-tg"
    target_type = "instance"
    protocol = "HTTP"
    vpc_id = aws_vpc.main.id
    port = 8080

    health_check {
        interval = 30
        path = "/healthz"
        protocol = "HTTP"
        healthy_threshold = 3
        unhealthy_threshold = 3
        timeout = 6
    }
}

