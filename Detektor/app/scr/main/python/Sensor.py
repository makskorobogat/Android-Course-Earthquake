# $Author: tmahr $
# $Rev: 6572 $
# $Date: 2020-06-24 20:04:02 +0200 (Mi, 24. Jun 2020) $

class Sensor:

    __objektZaehler = 0

    def __init__(self, welt, position):
        self.welt = welt
        self.position = position
        self.id = Sensor.__objektZaehler
        Sensor.__objektZaehler += 1
        self.stosswelle = None

    def __repr__(self):
        s = "Sensor " + str(self.id) + " " + str(self.position)
        if self.stosswelle != None:
            s += " " + str(self.stosswelle)
        return s

    def erfasseStosswelle(self, stosswelle):
        self.stosswelle = stosswelle
