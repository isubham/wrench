"""empty message

Revision ID: f474fde277e1
Revises: b2e019922d64
Create Date: 2020-05-27 09:06:19.931351

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = 'f474fde277e1'
down_revision = 'b2e019922d64'
branch_labels = None
depends_on = None


def upgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.add_column('Activity', sa.Column('purpose', sa.String(), nullable=True))
    # ### end Alembic commands ###


def downgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.drop_column('Activity', 'purpose')
    # ### end Alembic commands ###
