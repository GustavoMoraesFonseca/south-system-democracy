variable "amis" {
    default = {
        "us-east-1-ubuntu18" = "ami-09d56f8956ab235b3"
    }
}

variable "instance_type" {
    default = {
        "micro" = "t2.micro"
    }
}