# $Author: tmahr $
# $Rev: 6572 $
# $Date: 2020-06-24 20:04:02 +0200 (Mi, 24. Jun 2020) $

class Epizentrum:

    def __init__(self, welt, position):
        self.welt = welt
        self.position = position


    def __repr__(self):
        return "Epizentrum " + str(self.position)
