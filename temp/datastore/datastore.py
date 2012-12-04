import webapp2

from google.appengine.ext import db


#order is placed by user, but not confirmed by provider
STATUS_PENDING = "Pending"
#order is confirmed by provider, but not by courier
STATUS_PROV_CONFIRMED = "Provider Confirmed"
#order is confirmed by courier, but not confirmed by user
STATUS_COUR_CONFIRMED = "Courier Confirmed"
#order is completed, namely, user and courier completed transaction
STATUS_CLOSED = "Order Completed"



#========================================================================
# datastore entities
#========================================================================    
class FoodItems(db.Model):
    itemID = db.StringProperty(required = True)
    itemName = db.StringProperty(required = True)
    itemPrice = db.StringProperty(required = True)
    itemPicture = db.BlobProperty(default = None)
    date = db.DateTimeProperty(auto_now_add = True)

class Orders(db.Model):
    orderID = db.StringProperty(required = True)
    orderStatus = db.StringProperty(required = True)
    orderUser = db.StringProperty(required = True)
    date = db.DateTimeProperty(auto_now_add = True)    

class Users(db.Model):
    name = db.StringProperty(required = True)
    password = db.StringProperty(required = True)
    identity = db.StringProperty()


#========================================================================
# handlers
#========================================================================
# handle request for food items
class FoodItemsHandler(webapp2.RequestHandler):
    def get(self):
        self.response.headers['Content-Type'] = 'text/plain'
        for foodItem in FoodItems.all().run():
            response = ";".join([foodItem.itemID,
                                 foodItem.itemName,
                                 foodItem.itemPrice,
                                 str(foodItem.date)]) + "\n"
            self.response.write(response)

    def post(self):
        itemID = self.request.get("itemID")
        itemName = self.request.get("itemName")
        itemPrice = self.request.get("itemPrice")
        # Add new food item into database (no validation is performed)
        fooditem = FoodItems(itemID=itemID,
                             itemName=itemName,
                             itemPrice=itemPrice)
        fooditem.put()
        self.response.write(self.request)

        
class OrdersHandler(webapp2.RequestHandler):
    def get(self):
        self.response.headers['Content-Type'] = 'text/plain'
        
        user = self.request.get('orderUser')
        for order in Orders.all().filter('orderUser = ', user).run():
            response = ";".join([order.orderID,
                                 order.orderStatus,
                                 order.orderUser,
                                 str(order.date)]) + "\n"
            self.response.write(response)

    def post(self):
        orderID = self.request.get("orderID")
        orderStatus = self.request.get("orderStatus")
        orderUser = self.request.get("orderUser")
        # Add new food item into database (no validation is performed)
        if orderStatus == STATUS_PENDING:
            order = Orders(orderID=orderID,
                           orderStatus=orderStatus,
                           orderUser=orderUser)
            order.put()
        else:
            order = Orders.all().filter("orderID =", orderID).get()
            order.orderStatus = orderStatus
            db.put(order)

        
class UsersHandler(webapp2.RequestHandler):
    def get(self):
        self.response.headers['Content-Type'] = 'text/plain'
        name = self.request.get("name")
        password = self.request.get("password")

        user = Users.all().filter('name = ', name).get()
        if not user:
            self.response.write("error: no such user\n")
        elif password != user.password:
            self.response.write("error: wrong password\n")
        else:
            self.response.write(user.identity + "\n")


    def post(self):
        name = self.request.get("name")
        password = self.request.get("password")
        user = Users.all().filter('name = ', name).get()

        if user:
            self.response.write("error: user exits\n")
        else:
            new_user = Users(name=name,
                             password=password,
                             identity="customer")
            new_user.put()
            self.response.write("Thank for signing up!\n")

        
app = webapp2.WSGIApplication([('/fooditems', FoodItemsHandler),
                               ('/orders', OrdersHandler),
                               ('/users', UsersHandler)],
                              debug=True)
