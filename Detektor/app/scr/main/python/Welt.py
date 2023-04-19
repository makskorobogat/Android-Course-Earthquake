# $Author: tmahr $
# $Rev: 6572 $
# $Date: 2020-06-24 20:04:02 +0200 (Mi, 24. Jun 2020) $

import math
import random
from Epizentrum import Epizentrum
from Sensor import Sensor
from Stosswellenmessung import Stosswellenmessung
from Vector import Vector

class Welt:

    def __init__(self, xMin, xMax, yMin, yMax, anzahlSensoren, wellengeschwindigkeit):
        self.xMin = xMin
        self.xMax = xMax
        self.yMin = yMin
        self.yMax = yMax
        self.v = wellengeschwindigkeit
        self.sensoren = []
        for i in range(0,anzahlSensoren):
            pos = Vector(random.uniform(xMin,xMax), random.uniform(yMin, yMax))
            sensor = Sensor(self, pos)
            self.sensoren.append(sensor)
        self.epizentrum = Epizentrum(self, Vector(random.uniform(xMin,xMax), random.uniform(yMin, yMax)))

    def durchmesser(self): # Durchmesser der Welt
        dx = self.xMax-self.xMin
        dy = self.yMax-self.yMin
        return math.sqrt(dx*dx+dy*dy)

    def __repr__(self):
        s = "Welt x=[" + '{:.2f}'.format(self.xMin) + "," + '{:.2f}'.format(self.xMax) + "] y=[" \
               + '{:.2f}'.format(self.yMin) + "," + '{:.2f}'.format(self.yMax) + "] v=" + '{:.2f}'.format(self.v)
        s += "\n\t" + str(self.epizentrum)
        for sensor in self.sensoren:
            s += "\n\t" + str(sensor)
        return s

    def bebenAusloesen(self, zeit, amplitude):
        print("Beben auslösen um " +  '{:.2f}'.format(zeit) + " mit Staerke " + '{:.2f}'.format(amplitude))
        for sensor in self.sensoren:
            d = Vector.subtraction(self.epizentrum.position, sensor.position).mag()
            t = zeit + d / self.v
            messung = Stosswellenmessung(t,sensor.position,amplitude)
            sensor.erfasseStosswelle(messung)

    def liefereMessungen(self):
        m = []
        for s in self.sensoren:
            if s.stosswelle != None:
                m.append(s.stosswelle)
        return m
