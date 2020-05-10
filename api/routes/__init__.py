from flask import Blueprint
routes: Blueprint = Blueprint('routes', __name__)

from .license import *
from .user import *
from .people import *
from .activity import *


