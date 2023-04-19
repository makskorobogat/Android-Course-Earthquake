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

    def __init__(self, xAll, yAll, epi1, epi2, xMax, xMin, yMax, yMin, anzahlSensoren, wellengeschwindigkeit):
        self.xAll = xAll
        self.yMin = yMin
        self.yMax = yMax
        self.xMin = xMin
        self.xMax = xMax
        self.yAll = yAll
        self.v = wellengeschwindigkeit
        self.sensoren = []
        self.epi1 = epi1
        self.epi2 = epi2
        j = 0
        for i in range(0,anzahlSensoren-2):
            #print (xAll)
            #print(yAll)
            pos1 = random.uniform(xAll[j+1],xAll[j])
            #print (pos1)
            #print (xAll[j+1],xAll[j])
            pos2 = random.uniform(yAll[j+1],yAll[j])
            #print (pos2)
            #print(yAll[j+1],yAll[j])
            j += 2
            pos = Vector(pos1, pos2)
            sensor = Sensor(self, pos)
            self.sensoren.append(sensor)
            self.epizentrum = Epizentrum(self, Vector(epi1, epi2))

    def durchmesser(self): # Durchmesser der Welt
        dx = self.xMax-self.xMin
        dy = self.yMax-self.yMin
        return math.sqrt(dx*dx+dy*dy)

    def __repr__(self):
        s = "Welt x=[" + '{:.2f}'.format(self.xAll[1]) + "," + '{:.2f}'.format(self.xAll[1]) + "] y=[" \
               + '{:.2f}'.format(self.yAll[1]) + "," + '{:.2f}'.format(self.yAll[1]) + "] v=" + '{:.2f}'.format(self.v)
        s += "\n\t" + str(self.epizentrum)
        for sensor in self.sensoren:
            s += "\n\t" + str(sensor)
        return s

    def bebenAusloesen(self, All, zeit, amplitude):
        print("Beben auslösen um " +  '{:.2f}'.format(zeit) + " mit Staerke " + '{:.2f}'.format(amplitude))
        tAll = []
        for sensor in self.sensoren:
            d = Vector.subtraction(self.epizentrum.position, sensor.position).mag()
            t = zeit + d / self.v
            tAll.append(t)
            messung = Stosswellenmessung(t,sensor.position,amplitude)
            sensor.erfasseStosswelle(messung)

    def liefereMessungen(self):
        m = []
        for s in self.sensoren:
            if s.stosswelle != None:
                m.append(s.stosswelle)
        return m
