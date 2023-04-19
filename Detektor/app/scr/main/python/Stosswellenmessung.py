# $Author: tmahr $
# $Rev: 6572 $
# $Date: 2020-06-24 20:04:02 +0200 (Mi, 24. Jun 2020) $

class Stosswellenmessung:

    def __init__(self, zeit, position, amplitude):
        self.zeit = zeit
        self.position = position
        self.amplitude = amplitude

    def __repr__(self):
        return "Stosswellenmessung an " + str(self.position) + " um " + '{:.2f}'.format(self.zeit) + " mit Staerke " + '{:.2f}'.format(self.amplitude)
