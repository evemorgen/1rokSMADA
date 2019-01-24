import pygame
import random
from copy import copy
from math import cos, sin, pi, sqrt, hypot

from typing import List
Waypoint = List[int]


def random_vin(i=1234):
    used_vins = []
    while True:
        i = (i + random.randint(0, 1000)) % 99999
        if i not in used_vins:
            yield "VIN" + str(i)
            used_vins.append(i)


class Car(pygame.sprite.Sprite):
    def __init__(self, config, path=None, dir=None, image=None, supervisor=None):
        pygame.sprite.Sprite.__init__(self)
        self.image = pygame.image.load(image or random.choice(config["sprites"]))
        self.vin = next(random_vin())
        self.rect = self.image.get_rect()
        self.oryginal = copy(self.image)
        self.dir = dir or 0
        self.velocity: float = config["velocity"]
        self.acceleration: float = 0
        self.waypoints: List[Waypoint] = path or random.choice(config["paths"])
        self.waypoint_idx: int = 1
        self.current_waypoint: Waypoint = self.waypoints[1]
        self.rect.x, self.rect.y = self.waypoints[0]
        self.rect.x -= self.image.get_rect().size[0] / 2
        self.rect.y -= self.image.get_rect().size[1] / 2

    def next_waypoint(self) -> None:
        self.waypoint_idx += 1
        try:
            self.current_waypoint = self.waypoints[self.waypoint_idx]
        except IndexError:
            self.kill()

    def turn(self, amount: float) -> None:
        oldCenter = self.rect.center
        self.dir += amount
        self.image = pygame.transform.rotate(self.oryginal, 270 - self.dir)
        self.rect = self.image.get_rect()
        self.rect.center = oldCenter

    def towards_waypoint(self) -> None:
        (waypoint_x, waypoint_y) = self.current_waypoint
        (center_x, center_y) = self.rect.center
        waypoint_vector = pygame.math.Vector2(waypoint_x - center_x, waypoint_y - center_y)
        current_heading = pygame.math.Vector2(self.velocity, 0)
        current_heading.rotate_ip(self.dir)
        to_rotate = current_heading.angle_to(waypoint_vector)
        self.turn(to_rotate)

    def update(self) -> None:
        self.towards_waypoint()
        self.rect.x += cos(self.dir * pi / 180.0) * self.velocity
        self.rect.y += sin(self.dir * pi / 180.0) * self.velocity
        (waypoint_x, waypoint_y) = self.current_waypoint
        (center_x, center_y) = self.rect.center
        if hypot(waypoint_x - center_x, waypoint_y - center_y) <= self.velocity:
            self.next_waypoint()

    def __repr__(self):
        return f"Car(vin={self.vin}, x={self.rect.x}, y={self.rect.y}, v={self.velocity})"