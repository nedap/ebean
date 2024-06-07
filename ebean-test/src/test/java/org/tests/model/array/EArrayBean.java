package org.tests.model.array;


import io.ebean.annotation.Cache;
import io.ebean.annotation.DbArray;
import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Cache
public class EArrayBean {

  enum Status {
    ONE, TWO, THREE
  }

  IntEnum foo;

  @Id
  Long id;

  String name;

  @DbArray(length = 300)
  List<String> phoneNumbers = new ArrayList<>();

  @DbArray @NotNull
  List<UUID> uids = new ArrayList<>();

  @DbArray
  List<Long> otherIds = new ArrayList<>();

  @DbArray
  List<BigDecimal> decimals;

  @DbArray
  List<Double> doubs;
  @DbArray
  List<Float> floats;

  @DbArray(nullable = false)
  List<Status> statuses = new ArrayList<>();

  @DbArray
  List<VarcharEnum> vcEnums = new ArrayList<>();

  @DbArray @NotNull
  List<IntEnum> intEnums;

  @DbArray
  Set<Status> status2;

  @DbArray
  List<Instant> times;

  @DbArray
  List<LocalDate> dates;

  @Version
  Long version;

  public IntEnum getFoo() {
    return foo;
  }

  public void setFoo(final IntEnum foo) {
    this.foo = foo;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getPhoneNumbers() {
    return phoneNumbers;
  }

  public void setPhoneNumbers(List<String> phoneNumbers) {
    this.phoneNumbers = phoneNumbers;
  }

  public List<UUID> getUids() {
    return uids;
  }

  public void setUids(List<UUID> uids) {
    this.uids = uids;
  }

  public List<Long> getOtherIds() {
    return otherIds;
  }

  public void setOtherIds(List<Long> otherIds) {
    this.otherIds = otherIds;
  }

  public List<BigDecimal> getDecimals() {
    return decimals;
  }

  public void setDecimals(List<BigDecimal> decimals) {
    this.decimals = decimals;
  }

  public List<Double> getDoubs() {
    return doubs;
  }

  public void setDoubs(List<Double> doubs) {
    this.doubs = doubs;
  }

  public List<Float> getFloats() {
    return floats;
  }

  public void setFloats(List<Float> floats) {
    this.floats = floats;
  }

  public List<Status> getStatuses() {
    return statuses;
  }

  public void setStatuses(List<Status> statuses) {
    this.statuses = statuses;
  }

  public List<VarcharEnum> getVcEnums() {
    return vcEnums;
  }

  public void setVcEnums(final List<VarcharEnum> vcEnums) {
    this.vcEnums = vcEnums;
  }

  public List<IntEnum> getIntEnums() {
    return intEnums;
  }

  public void setIntEnums(final List<IntEnum> intEnums) {
    this.intEnums = intEnums;
  }

  public Set<Status> getStatus2() {
    return status2;
  }

  public void setStatus2(Set<Status> status2) {
    this.status2 = status2;
  }

  public List<Instant> getTimes() {
    return times;
  }

  public EArrayBean setTimes(List<Instant> times) {
    this.times = times;
    return this;
  }

  public List<LocalDate> getDates() {
    return dates;
  }

  public EArrayBean setDates(List<LocalDate> dates) {
    this.dates = dates;
    return this;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }
}
