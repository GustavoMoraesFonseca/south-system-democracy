resource "aws_security_group" "southsystem-secure" {
  name = "southsystem-secure"
  description = "SouthSystem Test by Gustavo"

  ingress { 
      cidr_blocks = [
          "0.0.0.0/0"
      ]
      description = "Para acessar a maquina com todas as portas"
      from_port = 0
      ipv6_cidr_blocks = [
          "::/0"
      ]
      prefix_list_ids = []
      protocol = "-1"
      security_groups = []
      self = false
      to_port = 0
  }

  egress {
      cidr_blocks = [
          "0.0.0.0/0"
      ]
      description = ""
      from_port = 0
      ipv6_cidr_blocks = [
          "::/0"
      ]
      prefix_list_ids = []
      protocol = "-1"
      security_groups = []
      self = false
      to_port = 0
  }

  tags = {
      Name = "ezops-secure"
  }
}