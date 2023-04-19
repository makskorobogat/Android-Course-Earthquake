# $Author: tmahr $
# $Rev: 6572 $
# $Date: 2020-06-24 20:04:02 +0200 (Mi, 24. Jun 2020) $

import math

class Vector:
    """
    A reimplementation of Processing's PVector class in Python.
    <br>
    This provides a simple 2D vector class, with the sole intention of making
    it easy to port code over.
    <br>
    This is essentially transliterated from the Processing project, and as
    such, should be Licensed under the 2.1 version of the GPL.
    <br>
    Copyright (c) 2008 Dan Shiffman
    Copyright (c) 2008-10 Ben Fry and Casey Reas
    Copyright (c) 2013 Nick Charlton
    """
    def __init__(self, x, y):
        self.x = x
        self.y = y
        # this holds the state for the iterable.
        self.index = 0

    def add(self, v):
        """
        Add another vector to this.
        """
        self.y = self.y + v.y
        self.x = self.x + v.x

    @staticmethod
    def addition(one, two):
        """
        Add two vectors. Returning the new one.
        """
        x = one.x + two.x
        y = one.y + two.y

        return Vector(x, y)

    def sub(self, v):
        """
        Subtract a vector from this.
        """
        self.x = self.x - v.x
        self.y = self.y - v.y

    @staticmethod
    def subtraction(one, two):
        """
        Subtract a vector from another.
        """
        x = one.x - two.x
        y = one.y - two.y

        return Vector(x, y)

    def mult(self, n):
        """
        Scale (multiply) this vector by a scalar.
        """
        self.x = self.x * n
        self.y = self.y * n

    @staticmethod
    def multiply(one, n):
        """
        Scale (multiply) a vector by a scalar.
        """
        x = one.x * n
        y = one.y * n

        return Vector(x, y)

    def div(self, d):
        """
        Scale (divide) this vector by a scalar.
        """
        self.x = self.x / d
        self.y = self.y / d

    @staticmethod
    def divide(one, d):
        """
        Scale (divide) a vector by a scalar.
        """
        x = one.x / d
        y = one.y / d

        return Vector(x, y)

    def mag(self):
        """
        Return the magnitude of the vector.
        """
        return math.sqrt(self.x * self.x + self.y * self.y)

    def magSquare(self):
        """
        Return the magnitude squared.
        """
        return (self.x * self.x + self.y * self.y)

    def normalize(self):
        """
        Normalize the vector.
        """
        m = self.mag()
        if (m != 0):
            self.div(m)

    def limit(self, max):
        """
        Limit the magnitude by a given scalar.
        """
        if (self.magSquare() > max * max):
            self.normalize()
            self.mult(max)

    def dot(self, v):
        """
        Return the dot product this and a given vector.
        """
        return self.x * v.x + self.y * v.y

    def heading(self):
        """
        The heading of the vector represented as an angle.
        """
        angle = math.atan2(-self.y, self.x)
        return -1 * angle

    def rotate(self, theta):
        """
        Rotate the vector by a given angle.
        """
        xTemp = self.x
        self.x = self.x * math.cos(theta) - self.y * math.sin(theta)
        self.y = xTemp * math.sin(theta) + self.y * math.cos(theta)

    def __repr__(self):
        return "[%f, %f]" % (self.x, self.y)

    def __iter__(self):
        return self

    def next(self):
        if self.index > 1:
            self.index = 0
            raise StopIteration

        self.index = self.index + 1
        if self.index == 1:
            return self.x
        else:
            return self.y
